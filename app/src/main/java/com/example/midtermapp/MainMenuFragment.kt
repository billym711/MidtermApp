package com.example.midtermapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.midtermapp.databinding.FragmentMainMenuBinding


/**
 * A simple [Fragment] subclass.
 * Use the [EditTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainMenuFragment : Fragment()   {
    val TAG = "TasksFragment"
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao
        val viewModelFactory = MainMenuViewModelFactory(dao)
        val viewModel = ViewModelProvider(
            this, viewModelFactory).get(MainMenuViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

       /* val adapter = TaskItemAdapter{ taskId ->
            viewModel.onTaskClicked(taskId)

        }*/
        val name = MainMenuFragmentArgs.fromBundle(requireArguments()).name
        val attempts = MainMenuFragmentArgs.fromBundle(requireArguments()).attempts
        val nameText = view.findViewById<TextView>(R.id.textView3)
        if (!name.isNullOrEmpty()){
            nameText.setText(name + " score: " + attempts + "\n Play Another Game?")
        }
        fun taskClicked (taskId : Long) {
            viewModel.onTaskClicked(taskId)
        }
        fun deleteClicked (taskId : Long) {
            Log.d(TAG, "in yesPressed(): taskId = $taskId")
            binding.viewModel?.deleteTask(taskId)
        }
        val adapter = TaskItemAdapter(::taskClicked,::deleteClicked)


        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToTask.observe(viewLifecycleOwner, Observer { taskId ->
            taskId?.let {
                val action =
                    com.example.midtermapp.MainMenuFragmentDirections.actionTasksFragmentToEditTaskFragment(
                        taskId
                    )
                this.findNavController().navigate(action)
                viewModel.onTaskNavigated()
            }
        })
        viewModel.navigateToScores.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_tasksFragment_to_scoresFragment)
            }
        })

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
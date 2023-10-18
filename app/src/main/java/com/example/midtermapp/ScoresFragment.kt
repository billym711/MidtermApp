package com.example.midtermapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.midtermapp.databinding.FragmentMainMenuBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ScoresFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoresFragment : Fragment()   {
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
        val back = view.findViewById<Button>(R.id.save_button)
        back.setOnClickListener {

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




        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
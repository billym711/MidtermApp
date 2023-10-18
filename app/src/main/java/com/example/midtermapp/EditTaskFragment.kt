package com.example.midtermapp

import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.midtermapp.databinding.FragmentEditTaskBinding
import kotlin.random.Random


/**
 */
class EditTaskFragment : Fragment() {
    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        val taskId = EditTaskFragmentArgs.fromBundle(requireArguments()).taskId

        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao

        val viewModelFactory = EditTaskViewModelFactory(taskId, dao)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditTaskViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_editTaskFragment_to_tasksFragment)
                viewModel.onNavigatedToList()
            }
        })
        var randomNum = Random.nextInt(0, 100)
        var okButton = view.findViewById<Button>(R.id.button)
        var minusButton = view.findViewById<ImageButton>(R.id.imageButton)
        var plusButton = view.findViewById<ImageButton>(R.id.imageButton2)
        var num = view.findViewById<EditText>(R.id.editTextText2)
        var attemptText = view.findViewById<TextView>(R.id.textView2)
        var name = view.findViewById<EditText>(R.id.editTextText)
        var attempts = 0

        okButton.setOnClickListener{
            if (!num.text.isNullOrEmpty()) {
                var mp = MediaPlayer.create(activity, Settings.System.DEFAULT_NOTIFICATION_URI);
                mp.start()
                if (num.text.toString().toInt() < randomNum) {
                    val toast =
                        Toast.makeText(activity, "Too Low", Toast.LENGTH_SHORT) // in Activity
                    toast.show()
                } else if (num.text.toString().toInt() > randomNum) {
                    val toast =
                        Toast.makeText(activity, "Too High", Toast.LENGTH_SHORT) // in Activity
                    toast.show()
                }
                attempts += 1
                attemptText.setText("Number of attempts: " + attempts.toString())
                Log.d("test", randomNum.toString())
            }

            if (num.text.toString() == randomNum.toString()){

                var action = com.example.midtermapp.EditTaskFragmentDirections.actionEditTaskFragmentToTasksFragment()
                    .setName(name.text.toString()).setAttempts(attempts.toString())
                view.findNavController()
                    .navigate(action)
            }
        }
        minusButton.setOnClickListener{
            if (num.text.toString() != ""){
                num.setText((num.text.toString().toInt() - 1).toString())
            }
        }
        plusButton.setOnClickListener{
            if (num.text.toString() != ""){
                num.setText((num.text.toString().toInt() + 1).toString())
            }
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
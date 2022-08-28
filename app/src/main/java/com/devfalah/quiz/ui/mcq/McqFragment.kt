import android.os.Bundle
import com.devfalah.quiz.ui.mcq.McqViewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.devfalah.quiz.R

class McqFragment:Fragment(R.layout.fragment_mcq) {
    private lateinit var viewModel: McqViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[McqViewModel::class.java]

        return super.onCreateView(inflater, container, savedInstanceState)
    }

}
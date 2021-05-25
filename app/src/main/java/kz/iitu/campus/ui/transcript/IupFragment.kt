package kz.iitu.campus.ui.transcript

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_academic_calendar.*
import kz.iitu.campus.R
import kz.iitu.campus.repository.StudyPlanRepository
import kz.iitu.campus.services.ApiFactory
import kz.iitu.campus.services.UserSession

class IupFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(
            this, IupViewModel.IupFactory(
                StudyPlanRepository(
                    ApiFactory.getApi()
                )
            )
        )[IupViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_study_plan, container, false)
        setupViews()
        setObservers()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studyPlanRV.layoutManager =
            LinearLayoutManager(this.context)

    }

    private fun setupViews() {
        val bearer: String = "Bearer " + this.context?.let { UserSession.getUserToken(it) }
        viewModel.getStudyPlan(bearer)
    }

    private fun setObservers() {
        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrBlank())
                Toast.makeText(
                    context,
                    it,
                    Toast.LENGTH_LONG
                ).show()
        })
        viewModel.studyPlan.observe(viewLifecycleOwner, Observer {
            studyPlanRV.adapter = StudyPlanRVA(it)
        })
    }
}
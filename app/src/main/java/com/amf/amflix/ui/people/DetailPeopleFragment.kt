package com.amf.amflix.ui.people

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.databinding.FragmentDetailPeopleBinding
import com.amf.amflix.repository.people.PeopleRepository
import com.amf.amflix.retrofit.models.people.PersonResponse
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailPeopleFragment : Fragment() {

    private var _binding: FragmentDetailPeopleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PersonDetailsViewModel by viewModels()
    private val repository = PeopleRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personId = arguments?.getInt("personId") ?: return
        Log.e("personita", personId.toString())

        repository.getPerson(personId)?.observe(viewLifecycleOwner, Observer { personResponse ->
            Log.e("personita", personResponse.toString())
            personResponse?.let {
                viewModel.setPerson(it)
                bindPersonDetails(it)
            }
        })

        binding.backarrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun bindPersonDetails(person: PersonResponse?) {
        person?.let {
            binding.personName.text = it.name
            binding.personBiography.text = it.biography
            binding.personBirthday.text = it.birthday
            binding.personPlaceOfBirth.text = it.place_of_birth
            binding.personKnownForDepartment.text = it.known_for_department

            Glide.with(this)
                .load(Constants.IMAGE_BASE_URL + it.profile_path)
                .override(600, 900)
                .placeholder(R.drawable.placeholder_load)
                .error(R.drawable.noperson)
                .into(binding.personThumbnail)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class PersonDetailsViewModel : ViewModel() {
    private val _person = MutableLiveData<PersonResponse>()
    val person: LiveData<PersonResponse> get() = _person

    fun setPerson(person: PersonResponse?) {
        _person.value = person!!
    }
}

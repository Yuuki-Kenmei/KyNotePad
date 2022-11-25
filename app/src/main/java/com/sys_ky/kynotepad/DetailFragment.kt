package com.sys_ky.kynotepad

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sys_ky.kynotepad.room.DbCtrl
import com.sys_ky.kynotepad.room.entity.Memo

class DetailFragment : Fragment() {

    private var mTextId: Int = -1
    private var mOldText: String = ""

    private var mCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTextId = it.getInt(MainActivity.cParamKey)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbCtrl = DbCtrl.getInstance(requireContext())
        mOldText = if (mTextId >= 0) {
            dbCtrl.Memo().selectTextById(mTextId)
        } else {
            ""
        }

        val editText = view.findViewById<EditText>(R.id.editText)
        editText.setText(mOldText, TextView.BufferType.EDITABLE)
        editText.requestFocus()
        editText.setSelection(0)
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

        val deleteButton = view.findViewById<FloatingActionButton>(R.id.deleteButton)
        if (mTextId < 0) {
            deleteButton.isVisible = false
            deleteButton.isEnabled = false
        }
        deleteButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.confirm)
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(
                    R.string.ok,
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dbCtrl.Memo().deleteById(mTextId)
                        Toast.makeText(requireContext(), R.string.delete, Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(view).popBackStack()
                    })
                .setNegativeButton(
                    R.string.cancel,
                    null)
                .show()
        }

        val fixButton = view.findViewById<FloatingActionButton>(R.id.fixButton)
        fixButton.setOnClickListener {
            val updText = requireView().findViewById<EditText>(R.id.editText).text.toString()
            if (mOldText != updText) {
                var updId: Int = 0
                if (mTextId < 0) {
                    if (dbCtrl.Memo().selectExistsIdZero()) {
                        updId = dbCtrl.Memo().selectNextId()
                    }
                    val sortId = dbCtrl.Memo().selectMaxSortId() + 1
                    dbCtrl.Memo().insertAll(Memo(updId, sortId, updText))
                } else {
                    updId = mTextId
                    dbCtrl.Memo().updateTextById(updId, updText)
                }
                Toast.makeText(requireContext(), R.string.save, Toast.LENGTH_SHORT).show()
            }
            Navigation.findNavController(view).popBackStack()
        }

        mCallback = requireActivity().onBackPressedDispatcher.addCallback {
            if (mOldText != requireView().findViewById<EditText>(R.id.editText).text.toString()) {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.confirm)
                    .setMessage(R.string.confirm_back)
                    .setPositiveButton(
                        R.string.ok,
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            Navigation.findNavController(view).popBackStack()
                        })
                    .setNegativeButton(
                        R.string.cancel,
                        null)
                    .show()
            } else {
                Navigation.findNavController(view).popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mCallback?.remove()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
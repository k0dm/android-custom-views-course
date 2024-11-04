package com.techyourchance.androidviews.exercises._01_

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.techyourchance.androidviews.general.BaseFragment
import com.techyourchance.androidviews.R
import com.techyourchance.androidviews.exercises._03_.SliderChangeListener

class MySliderFragment : BaseFragment() {

    override val screenName get() = getString(R.string.screen_name_my_slider)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return layoutInflater.inflate(R.layout.layout_my_slider, container, false)
    }

    lateinit var mySlider: MySliderView
    lateinit var sliderValueTextView: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mySlider = view.findViewById<MySliderView>(R.id.mySliderView)
        sliderValueTextView = view.findViewById<TextView>(R.id.sliderValueTextView)



        mySlider.setOnValueChangeListener(object : SliderChangeListener {
            override fun onValueChanged(value: Float) {
                sliderValueTextView.text = value.toString()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        sliderValueTextView.text = mySlider.value.toString()

    }

    companion object {
        fun newInstance(): MySliderFragment {
            return MySliderFragment()
        }
    }

}
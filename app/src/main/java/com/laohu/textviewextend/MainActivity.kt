package com.laohu.textviewextend

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initEllipseTextView()
        initTextView()
        initBlankTextView()
        initNormalTextView()
    }

    private fun initEllipseTextView() {
        val text = "手机（含具体配置、相关细节等）以门店展示、销售的具体产品为准。本产品网页中提及的所有价格（包括配置、配件等）均为手机厂商建议零售价格，欢迎各位用户购买。手机（含具体配置、相关细节等）以门店展示、销售的具体产品为准。本产品网页中提及的所有价格（包括配置、配件等）均为手机厂商建议零售价格，欢迎各位用户购买。"
        ellipsisEndTextView.text = text
        ellipsisEndTextView.setOnEllipsizeClickListener {
            Toast.makeText(this@MainActivity, "click", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initTextView() {
        val text = "手机（含具体配置、相关细节等）以门店展示、销售的具体产品为准。本产品网页中提及的所有价格（包括配置、配件等）均为手机厂商建议零售价格，欢迎各位用户购买。"
        newLineEllipsisView.text = text
    }

    private fun initBlankTextView() {
        val text = "手机（含具体配置、相关细节等）以门店展示、销售的具体产品为准。本产品网页中提及的所有价格（包括配置、配件等）均为手机厂商建议零售价格，欢迎各位用户购买。"
        blankEllipsisView.text = text
    }

    private fun initNormalTextView() {
        val text = "手机（含具体配置、相关细节等）以门店展示、销售的具体产品为准。本产品网页中提及的所有价格（包括配置、配件等）均为手机厂商建议零售价格，欢迎各位用户购买。"
        normalView.text = text
    }
}

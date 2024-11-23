package com.storeonline.application.config

import android.annotation.SuppressLint
import android.app.Application
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.UIConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

@SuppressLint("NewApi")
class PayPalConfig(application: Application) {

    init {
        val config = CheckoutConfig(
            application = application,
            clientId = "AVMEs3hBW-NrbxHm7dtvWLvszYEtK21THm-bnGzwyuTISIVEs20aPeM3SoMpCfDUmhmhcZd-8-WThzGG",
            environment = Environment.SANDBOX,
            returnUrl = "com.storeonline://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            uiConfig = UIConfig(

            )
        )
        PayPalCheckout.setConfig(config)
    }
}
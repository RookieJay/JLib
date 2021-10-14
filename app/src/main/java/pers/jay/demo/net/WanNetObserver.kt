package pers.jay.demo.net

import pers.jay.demo.data.EntityDemo
import pers.jay.library.network.rxjava.BaseNetObserver

class WanNetObserver: BaseNetObserver<EntityDemo>() {

    override fun handleExceptionFromServer(t: EntityDemo): Boolean {
        if (t.name == "222") {
            onException("error")
        }
        return true
    }

    override fun onSuccess(t: EntityDemo) {

    }

    override fun onException(errorMsg: String) {

    }

}
package alcala.jose.practica9

import kotlin.plus

data class User(var firstName:String?=null, var lastName:String?=null, var age:String?=null){
    override fun toString()=firstName+"\t"+lastName+"\t"+age
}

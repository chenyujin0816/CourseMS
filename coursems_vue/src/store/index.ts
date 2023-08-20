import { createStore } from 'vuex'

export default createStore({
  state:{
    token:'',
    username:'',
    phoneNumber:''
  },
  mutations:{
    SET_TOKEN: (state,token)=>{
      state.token=token;
      localStorage.setItem("token",token)
    },
    REMOVE_INFO:(state)=>{
      state.token=''
      localStorage.setItem("token",'')
    },
    SET_USERNAME:(state,username)=>{
      state.username=username;
      localStorage.setItem("username",username)
    }
  },
  getters:{
    getToken: state=>{
      return state.token
    }
  },
  actions:{

  },
  modules:{

  }
})

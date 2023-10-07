import Vuex from 'vuex'
import Vue from 'vue'
import { getToken, setToken, removeToken } from '@/request/token'
import { login, getUserInfo, logout, register } from '@/api/login'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    id: '',
    name: '',
    avatar: '',
    addr:'',
    token: getToken(),
    selecttable: []//搜索框搜索到的数据
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ADDR:(state, addr) => {
      state.addr = addr
    },
    SET_ID: (state, id) => {
      state.id = id
    },
    SET_SELECTTABLE: (state, selecttable) => {
      state.selecttable = selecttable
    },
  },
  actions: {
    login({ commit }, user) {
      return new Promise((resolve, reject) => {
        login(user.userName, user.userPassword).then(data => {
          if (data.success) {
            commit('SET_TOKEN', data.data)
            setToken(data.data)
            resolve()
          } else {
            reject(data.msg)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取用户信息
    getUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(data => {
          if (data.success) {
            commit('SET_NAME', data.data.userName)
            commit('SET_AVATAR', data.data.userAvatarUrl)
            commit('SET_ID', data.data.userNid.toString())
            commit('SET_ADDR',data.data.userAddr)
            resolve(data)
          } else {
            commit('SET_NAME', '')
            commit('SET_AVATAR', '')
            commit('SET_ID', '')
            commit('SET_ADDR','')
            removeToken()
            resolve(data)
          }

        }).catch(error => {
          // commit('SET_ACCOUNT', '')
          commit('SET_NAME', '')
          commit('SET_AVATAR', '')
          commit('SET_ID', '')
          commit('SET_ADDR','')
          removeToken()
          reject(error)
        })
      })
    },
    // 退出
    logout({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(data => {
          if (data.success) {

            commit('SET_TOKEN', '')
            // commit('SET_ACCOUNT', '')
            commit('SET_NAME', '')
            commit('SET_AVATAR', '')
            commit('SET_ID', '')
            commit('SET_ADDR','')
            removeToken()
            resolve()
          }

        }).catch(error => {
          reject(error)
        })
      })
    },
    // 前端 登出
    fedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        // commit('SET_ACCOUNT', '')
        commit('SET_NAME', '')
        commit('SET_AVATAR', '')
        commit('SET_ID', '')
        commit('SET_ADDR','')
        removeToken()
        resolve()
      }).catch(error => {
        reject(error)
      })
    },
    register({ commit }, user) {
      return new Promise((resolve, reject) => {
        register(user).then((data) => {
          if (data.success) {
            commit('SET_TOKEN', data.data)
            setToken(data.data)
            resolve()
          } else {
            reject(data.msg)
          }
        }).catch((error) => {
          reject(error)
        })
      })
    }
  }
})

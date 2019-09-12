import NOTIF_CONSTANTS from '@/consts/notification.js'

const state = {
  // currentNotification: {
  //   event: '',
  //   fileUrl: '',
  //   fileName: '',
  //   bulkSaveOperationTimestamp: '',
  //   bulkSaveOperationUuid: '',
  //   ingestionUrl: ''
  // },
  notifications: []
}

const mutations = {
  PUSH_NOTIFICATION: (state, notification) => {
    state.notifications.push(notification)
  },
  SHIFT_NOTIFICATION: (state) => {
    state.notifications.shift()
  }
}

const actions = {
  pushNotification({ commit, state }, notification) {
    const colors = ['#FF6633', '#FFB399', '#FF33FF', '#FFFF99', '#00B3E6', '#999966']
    let color
    if (notification.event !== 'alive') {
      let msg = ''
      switch (notification.event) {
        case NOTIF_CONSTANTS.FILE_FOUND:
          msg = `Fichier trouvé: ${notification.fileUrl.substring(notification.fileUrl.lastIndexOf('/')+1)}`
          color = colors[0]
          break
        case NOTIF_CONSTANTS.FILE_DOWNLOADED:
          msg = `Fichier téléchargé: ${notification.fileUrl.substring(notification.fileUrl.lastIndexOf('/')+1)}`
          color = colors[1]
          break
        case NOTIF_CONSTANTS.PROCESSING_STARTED:
          msg = 'Indexation a commencé.'
          color = colors[2]
          break
        case NOTIF_CONSTANTS.PROCESSING_ENDED:
          this._vm.$notify({
            title: 'Info',
            message: 'Processing ended'
          })
          msg = 'Indexation a terminé.'
          color = colors[3]
          break
        case NOTIF_CONSTANTS.FILE_INDEXED:
          msg = `Fichier indexé: ${notification.fileUrl.substring(notification.fileUrl.lastIndexOf('/')+1)}`
          color = colors[4]
          break
        case NOTIF_CONSTANTS.INGESTION_STARTED:
          msg = `Ingestion a commencé.`
          color = colors[5]
          break
        case '':
          msg = ''
          break
      }
      msg = `[${new Date(notification.timestamp).toLocaleString()}] ${msg}`
      notification = { msg, color}
      commit('PUSH_NOTIFICATION', notification)
      if (state.notifications.length === 300) {
        commit('SHIFT_NOTIFICATION')
      }
    }
  }
}

const getters = {
  getNotifications: state => {
    return state.notifications
  },
  getCurrentNotification: state => {
    return state.notifications[state.notifications.length - 1]
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}

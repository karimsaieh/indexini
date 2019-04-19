import NOTIF_CONSTANTS from '@/consts/notification.js'

const state = {
  currentNotification: {
    event: '',
    fileUrl: '',
    fileName: '',
    bulkSaveOperationTimestamp: '',
    bulkSaveOperationUuid: '',
    ingestionUrl: ''
  }
}

const mutations = {
  SET_CURRENT_NOTIFICATION: (state, notification) => {
    state.currentNotification = Object.assign({}, state.currentNotification, notification)
  },
  // ADD_ONGOING_INGESTION: (state, bulkSaveOperationUuid) => {
  //   state.onGoingIngestion.push(bulkSaveOperationUuid)
  // },
  // REMOVE_ONGOING_INGESTION: (state, bulkSaveOperationUuid) => {
  //   state.onGoingIngestion = state.onGoingIngestion.filter(item => item !== bulkSaveOperationUuid)
  // }
  DELETE_CURRENT_NOTIFICATION: (state) => {
    state.currentNotification = {}
  }
}

const actions = {
  updateCurrentNotification({ commit }, notification) {
    // if (state.onGoingIngestion.indexOf(notification.bulkSaveOperationUuid) > -1) {
    //   if (notification.event === INGESTION_ENDED) {
    //     commit('REMOVE_ONGOING_INGESTION', notification.bulkSaveOperationUuid)
    //   }
    // } else {
    //   if()
    //   commit('ADD_ONGOING_INGESTION', notification.bulkSaveOperationUuid)
    // }
    // // if ([FILE_FOUND, FILE_DOWNLOADED, INGESTION_STARTED, INGESTION_ENDED].indexOf(notification.event) > -1){

    // // }
    if (notification.event !== 'alive') {
      commit('SET_CURRENT_NOTIFICATION', notification)
    }
  },
  deleteCurrentNotification({ commit }) {
    commit('DELETE_CURRENT_NOTIFICATION')
  }
}

const getters = {
  getCurrentNotification: state => {
    let msg = ''
    switch (state.currentNotification.event) {
      case NOTIF_CONSTANTS.FILE_FOUND:
        msg = `Fichier trouvé: ${state.currentNotification.fileUrl}`
        break
      case NOTIF_CONSTANTS.FILE_DOWNLOADED:
        msg = `Fichier téléchargé: ${state.currentNotification.fileUrl}`
        break
      case NOTIF_CONSTANTS.PROCESSING_STARTED:
        msg = 'Indexation a commencé.'
        break
      case NOTIF_CONSTANTS.PROCESSING_ENDED:
        msg = 'Indexation a terminé.'
        break
      case NOTIF_CONSTANTS.FILE_INDEXED:
        msg = `Fichier indexé: ${state.currentNotification.fileUrl}`
        break
      case NOTIF_CONSTANTS.INGESTION_STARTED:
        msg = `Ingestion a commencé: Initialisé le ${state.currentNotification.bulkSaveOperationTimestamp}`
        break
      case '':
        msg = ''
        break
    }
    return msg
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}

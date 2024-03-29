const getModuleState = state => state.reservation;

export const getMenu = state => 
    getModuleState(state).menu;

export const getLastReserveId = state =>
    getModuleState(state).lastReserveId;

export const getReserveSearch = state =>
    getModuleState(state).reserveSearch;

export const getReserve = state =>
    getModuleState(state).reserve;

export const getUserEventEvaluationSearch = state =>
    getModuleState(state).userEventEvaluationSearch;

export const getCompanyEventEvaluationSearch = state =>
    getModuleState(state).companyEventEvaluationSearch;

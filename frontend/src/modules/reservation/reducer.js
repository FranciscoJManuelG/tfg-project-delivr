import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

import users from '../users';

const initialState = {
    menu: null,
    lastReserveId: null,
    reserveSearch: null,
    reserve: null,
    userEventEvaluationSearch: null,
    companyEventEvaluationSearch: null,
};

const menu = (state = initialState.menu, action) => {

    switch (action.type) {

        case users.actionTypes.LOGIN_COMPLETED:
            return action.authenticatedUser.menu;

        case users.actionTypes.SIGN_UP_COMPLETED:
            return action.authenticatedUser.menu;

        case actionTypes.MENU_UPDATED:
            return action.menu;

        case actionTypes.FIND_MENU_PRODUCTS_COMPLETED:
            return action.menu;

        case actionTypes.RESERVATION_COMPLETED:
            return {id: state.id, items: [], totalPrice: 0, totalQuantity: 0};

        case actionTypes.CALCULATE_DEPOSIT_COMPLETED:
            return {id: state.id, items: state.items, deposit: action.price, totalPrice: state.totalPrice, totalQuantity: state.totalQuantity};
        
        default:
            return state;

    }

}

const lastReserveId = (state = initialState.lastReserveId, action) => {

    switch (action.type) {

        case actionTypes.RESERVATION_COMPLETED:
            return action.reserveId;

        default:
            return state;

    }

}

const reserveSearch = (state = initialState.reserveSearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_RESERVES_COMPLETED:
            return action.reserveSearch;

        case actionTypes.CLEAR_RESERVE_SEARCH:
            return initialState.reserveSearch;

        case actionTypes.CANCEL_RESERVATION_COMPLETED:
            return { criteria: state.criteria, 
                result: {
                        items: state.result.items.filter
                        (reserve => reserve.id !== action.reserveId), 
                    existMoreItems : state.result.existMoreItems} };

        case actionTypes.REMOVE_RESERVATION_COMPLETED:
            return { criteria: state.criteria, 
                result: {
                        items: state.result.items.filter
                        (reserve => reserve.id !== action.reserveId), 
                    existMoreItems : state.result.existMoreItems} };

        default:
            return state;

    }

}

const reserve = (state = initialState.reserve, action) => {

    switch (action.type) {

        case actionTypes.FIND_RESERVE_COMPLETED:
            return action.reserve;

        case actionTypes.CLEAR_RESERVE:
            return initialState.reserve;

        default:
            return state;

    }

}

const userEventEvaluationSearch = (state = initialState.userEventEvaluationSearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_USER_EVENT_EVALUATIONS_COMPLETED:
            return action.userEventEvaluationSearch;

        case actionTypes.CLEAR_USER_EVENT_EVALUATION_SEARCH:
            return initialState.userEventEvaluationSearch;

        default:
            return state;

    }

}

const companyEventEvaluationSearch = (state = initialState.companyEventEvaluationSearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_COMPANY_EVENT_EVALUATIONS_COMPLETED:
            return action.companyEventEvaluationSearch;

        case actionTypes.CLEAR_COMPANY_EVENT_EVALUATION_SEARCH:
            return initialState.companyEventEvaluationSearch;

        default:
            return state;

    }

}

const reducer = combineReducers({
    menu,
    lastReserveId,
    reserveSearch,
    reserve,
    userEventEvaluationSearch,
    companyEventEvaluationSearch
});

export default reducer;

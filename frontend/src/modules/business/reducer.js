import {combineReducers} from 'redux';

import users from '../users';
import * as actionTypes from './actionTypes';

const initialState = {
    companyCategories: null,
    company: null,
    cities: null,
    companyAddressSearch: null,
};

const company = (state = initialState.company, action) => {

    switch (action.type) {

        case actionTypes.ADDED_COMPANY_COMPLETED:
            return action.company;
        
        case actionTypes.MODIFY_COMPANY_COMPLETED:
            return action.company;

        case actionTypes.COMPANY_ADDRESS_DELETED:
            return action.company;

        case actionTypes.BLOCK_COMPANY_COMPLETED:
            return action.company;

        case actionTypes.UNLOCK_COMPANY_COMPLETED:
            return action.company;

        case actionTypes.FIND_COMPANY_COMPLETED:
            return action.company;
    

        case users.actionTypes.LOGOUT:
            return initialState.company;

        default:
            return state;

    }

}

const companyCategories = (state = initialState.companyCategories, action) => {

    switch (action.type) {

        case actionTypes.FIND_ALL_COMPANY_CATEGORIES_COMPLETED:
            return action.companyCategories;

        default:
            return state;

    }

}

const companyAddressSearch = (state = initialState.companyAddressSearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_COMPANY_ADDRESSES_COMPLETED:
            return action.companyAddressSearch;

        case actionTypes.CLEAR_COMPANY_ADDRESS_SEARCH:
            return initialState.companyAddressSearch;

        default:
            return state;

    }

}

const cities = (state = initialState.cities, action) => {

    switch (action.type) {

        case actionTypes.FIND_ALL_CITIES_COMPLETED:
            return action.cities;

        default:
            return state;

    }

}

const reducer = combineReducers({
    companyCategories,
    company,
    cities,
    companyAddressSearch
});

export default reducer;


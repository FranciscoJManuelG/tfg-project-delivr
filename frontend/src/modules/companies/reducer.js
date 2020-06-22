import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    companyCategories: null,
    company: null,
};

const company = (state = initialState.company, action) => {

    switch (action.type) {

        case actionTypes.ADDED_COMPANY_COMPLETED:
            return action.company;
        
        case actionTypes.MODIFY_COMPANY_COMPLETED:
            return action.company;

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

const reducer = combineReducers({
    companyCategories,
    company
});

export default reducer;


import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    companyCategories: null,
};

const companyCategories = (state = initialState.companyCategories, action) => {

    switch (action.type) {

        case actionTypes.FIND_ALL_COMPANY_CATEGORIES_COMPLETED:
            return action.companyCategories;

        default:
            return state;

    }

}

const reducer = combineReducers({
    companyCategories
});

export default reducer;


import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    companySearch: null
};

const companySearch = (state = initialState.companySearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_COMPANIES_COMPLETED:
            return action.companySearch;

        case actionTypes.CLEAR_COMPANY_SEARCH:
            return initialState.companySearch;

        default:
            return state;

    }

}

const reducer = combineReducers({
    companySearch
});

export default reducer;


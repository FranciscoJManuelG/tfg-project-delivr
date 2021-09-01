import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';
import * as businessActionTypes from '../business/actionTypes';

const initialState = {
    companySearch: null
};

const companySearch = (state = initialState.companySearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_COMPANIES_COMPLETED:
            return action.companySearch;

        case actionTypes.CLEAR_COMPANY_SEARCH:
            return initialState.companySearch;

        case businessActionTypes.COMPANY_DELETED:
            return { criteria: state.criteria, 
                result: {
                     items: state.result.items.filter
                        (company => company.id !== action.companyId), 
                    existMoreItems : state.result.existMoreItems} };

        default:
            return state;

    }

}

const reducer = combineReducers({
    companySearch
});

export default reducer;


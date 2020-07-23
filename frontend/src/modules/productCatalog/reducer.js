import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    productSearch: null,
    productCategories: null
};

const productSearch = (state = initialState.productSearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_PRODUCTS_COMPLETED:
            return action.productSearch;

        case actionTypes.CLEAR_PRODUCT_SEARCH:
            return initialState.productSearch;

        default:
            return state;

    }

}

const productCategories = (state = initialState.productCategories, action) => {

    switch (action.type) {

        case actionTypes.FIND_COMPANY_PRODUCT_CATEGORIES_COMPLETED:
            return action.productCategories;

        default:
            return state;

    }

}

const reducer = combineReducers({
    productSearch,
    productCategories
});

export default reducer;


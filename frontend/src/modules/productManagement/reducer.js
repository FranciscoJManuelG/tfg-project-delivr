import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    productSearch: null,
    productCategories: null,
    product: null
};

const productSearch = (state = initialState.productSearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_PRODUCTS_COMPLETED:
            return action.productSearch;

        case actionTypes.CLEAR_PRODUCT_SEARCH:
            return initialState.productSearch;

        case actionTypes.PRODUCT_DELETED:
            return state.filter(
                product => product.id !== action.productId
            )

        case actionTypes.BLOCK_PRODUCT_COMPLETED:
            return {criteria: state.criteria,
                result: {
                    items:state.result.items.map(
                        function(product) { return product.id === action.product.id ? action.product : product;}
                    ), existMoreItems: state.result.existMoreItems
                }};

        case actionTypes.UNLOCK_PRODUCT_COMPLETED:
            return {criteria: state.criteria,
                result: {
                    items: state.result.items.map(
                        function(product) { return product.id === action.product.id ? action.product : product;}
                    ), existMoreItems: state.result.existMoreItems
                }};

        default:
            return state;

    }

}

const productCategories = (state = initialState.productCategories, action) => {

    switch (action.type) {

        case actionTypes.FIND_ALL_PRODUCT_CATEGORIES_COMPLETED:
            return action.productCategories;

        default:
            return state;

    }

}

const product = (state = initialState.product, action) => {

    switch (action.type) {

        case actionTypes.FIND_PRODUCT_COMPLETED:
            return action.product;

        case actionTypes.CLEAR_PRODUCT:
            return initialState.product;

        default:
            return state;

    }

}


const reducer = combineReducers({
    productSearch,
    productCategories,
    product
});

export default reducer;


import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

import users from '../users';

const initialState = {
    shoppingCart: null
};

const shoppingCart = (state = initialState.shoppingCart, action) => {

    switch (action.type) {

        case users.actionTypes.LOGIN_COMPLETED:
            return action.authenticatedUser.shoppingCart;

        case users.actionTypes.SIGN_UP_COMPLETED:
            return action.authenticatedUser.shoppingCart;

        case actionTypes.SHOPPING_CART_UPDATED:
            return action.shoppingCart;
    
        default:
            return state;

    }

}





const reducer = combineReducers({
    shoppingCart
});

export default reducer;

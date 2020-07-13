import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    user: null,
    favouriteAddressSearch: null
};

const user = (state = initialState.user, action) => {

    switch (action.type) {

        case actionTypes.SIGN_UP_COMPLETED:
            return action.authenticatedUser.user;

        case actionTypes.LOGIN_COMPLETED:
            return action.authenticatedUser.user;

        case actionTypes.LOGOUT:
            return initialState.user;

        case actionTypes.UPDATE_PROFILE_COMPLETED:
            return action.user;

        default:
            return state;

    }

}

const favouriteAddressSearch = (state = initialState.favouriteAddressSearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_FAVOURITE_ADDRESSES_COMPLETED:
            return action.favouriteAddressSearch;

        case actionTypes.CLEAR_FAVOURITE_ADDRESS_SEARCH:
            return initialState.favouriteAddressSearch;

        case actionTypes.FAVOURITE_ADDRESS_DELETED:
            return { criteria: state.criteria, 
                result: {
                     items: state.result.items.filter
                        (favouriteAddress => favouriteAddress.id !== action.addressId), 
                    existMoreItems : state.result.existMoreItems} };

        default:
            return state;

    }

}

const reducer = combineReducers({
    user,
    favouriteAddressSearch
});

export default reducer;



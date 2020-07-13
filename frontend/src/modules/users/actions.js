import * as actionTypes from './actionTypes';
import backend from '../../backend';
import * as businessActions from '../business/actions';

const signUpCompleted = authenticatedUser => ({
    type: actionTypes.SIGN_UP_COMPLETED,
    authenticatedUser
});

export const signUp = (user, onSuccess, onErrors, reauthenticationCallback) => dispatch =>
    backend.userService.signUp(user,
        authenticatedUser => {
            dispatch(signUpCompleted(authenticatedUser));
            onSuccess();
        },
        onErrors,
        reauthenticationCallback);
        
export const signUpBusinessman = (user, onSuccess, onErrors, reauthenticationCallback) => dispatch =>
backend.userService.signUpBusinessman(user,
    authenticatedUser => {
        dispatch(signUpCompleted(authenticatedUser));
        onSuccess();
    },
    onErrors,
    reauthenticationCallback);
    
const loginCompleted = authenticatedUser => ({
    type: actionTypes.LOGIN_COMPLETED,
    authenticatedUser
});

export const login = (userName, password, onSuccess, onErrors, reauthenticationCallback) => dispatch =>
    backend.userService.login(userName, password,
        authenticatedUser => {
            dispatch(loginCompleted(authenticatedUser));
            onSuccess();
            backend.businessService.findCompany(
                company => {
                    dispatch(businessActions.findCompanyCompleted(company));
                }
            );
        },
        onErrors,
        reauthenticationCallback
    );

export const tryLoginFromServiceToken = reauthenticationCallback => dispatch =>
    backend.userService.tryLoginFromServiceToken(
        authenticatedUser => {
            if (authenticatedUser) {
                dispatch(loginCompleted(authenticatedUser));
                backend.businessService.findCompany(
                    company => {
                        dispatch(businessActions.findCompanyCompleted(company));
                    }
                );
            }
            
        },
        reauthenticationCallback
    );
    

export const logout = () => {

    backend.userService.logout();

    return {type: actionTypes.LOGOUT};

};

export const updateProfileCompleted = user => ({
    type: actionTypes.UPDATE_PROFILE_COMPLETED,
    user
})

export const updateProfile = (user, onSuccess, onErrors) => dispatch =>
    backend.userService.updateProfile(user, 
        user => {
            dispatch(updateProfileCompleted(user));
            onSuccess();
        },
        onErrors);

export const changePassword = (id, oldPassword, newPassword, onSuccess, onErrors) => dispatch =>
    backend.userService.changePassword(id, oldPassword, newPassword, onSuccess, onErrors);

const addedFavouriteAddressCompleted = address => ({
    type: actionTypes.ADDED_FAVOURITE_ADDRESS_COMPLETED,
    address
});

export const addFavouriteAddress = (street, cp, cityId, onSuccess, onErrors) => dispatch =>
    backend.userService.addFavouriteAddress(street, cp, cityId, 
        address => {
            dispatch(addedFavouriteAddressCompleted(address));
            onSuccess();
        },
        onErrors);

const findFavouriteAddressesCompleted = favouriteAddressSearch => ({
    type: actionTypes.FIND_FAVOURITE_ADDRESSES_COMPLETED,
    favouriteAddressSearch
});

const clearFavouriteAddressSearch = () => ({
    type: actionTypes.CLEAR_FAVOURITE_ADDRESS_SEARCH
});


export const findFavouriteAddresses = (criteria) => dispatch => {

    dispatch(clearFavouriteAddressSearch());
    backend.userService.findFavouriteAddresses(criteria, 
        result => dispatch(findFavouriteAddressesCompleted({criteria, result})));

}

export const previousFindFavouriteAddressesResultPage = (criteria) => 
    findFavouriteAddresses({page: criteria.page-1});

export const nextFindFavouriteAddressesResultPage = (criteria) => 
    findFavouriteAddresses({page: criteria.page+1});

const favouriteAddressDeleted = addressId => ({
    type: actionTypes.FAVOURITE_ADDRESS_DELETED,
    addressId
});

export const deleteFavouriteAddress = (addressId, onSuccess, 
    onErrors) => dispatch => 
    backend.userService.deleteFavouriteAddress(addressId,
        () => {
            dispatch(favouriteAddressDeleted(addressId));
            onSuccess();
        },
        onErrors);


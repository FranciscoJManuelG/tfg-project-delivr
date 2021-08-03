import {config, appFetch, setServiceToken, getServiceToken, removeServiceToken, setReauthenticationCallback} from './appFetch';

export const login = (userName, password, onSuccess, onErrors, reauthenticationCallback) =>
    appFetch('/users/login', config('POST', {userName, password}),
        authenticatedUser => {
            setServiceToken(authenticatedUser.serviceToken);
            setReauthenticationCallback(reauthenticationCallback);
            onSuccess(authenticatedUser);
        }, 
        onErrors);

export const tryLoginFromServiceToken = (onSuccess, reauthenticationCallback) => {

    const serviceToken = getServiceToken();

    if (!serviceToken) {
        onSuccess();
        return;
    }

    setReauthenticationCallback(reauthenticationCallback);

    appFetch('/users/loginFromServiceToken', config('POST'),
        authenticatedUser => onSuccess(authenticatedUser),
        () => removeServiceToken()
    );

}

export const signUp = (user, onSuccess, onErrors, reauthenticationCallback) => {

    appFetch('/users/signUp', config('POST', user), 
        authenticatedUser => {
            setServiceToken(authenticatedUser.serviceToken);
            setReauthenticationCallback(reauthenticationCallback);
            onSuccess(authenticatedUser);
        }, 
        onErrors);

}

export const signUpBusinessman = (user, onSuccess, onErrors, reauthenticationCallback) => {

    appFetch('/users/signUpBusinessman', config('POST', user), 
        authenticatedUser => {
            setServiceToken(authenticatedUser.serviceToken);
            setReauthenticationCallback(reauthenticationCallback);
            onSuccess(authenticatedUser);
        }, 
        onErrors);

}

export const logout = () => removeServiceToken();

export const updateProfile = (user, onSuccess, onErrors) =>
    appFetch(`/users/${user.id}`, config('PUT', user),
        onSuccess, onErrors);

export const changePassword = (id, oldPassword, newPassword, onSuccess,
    onErrors) =>
    appFetch(`/users/${id}/changePassword`, 
        config('POST', {oldPassword, newPassword}),
        onSuccess, onErrors);

export const addFavouriteAddress = (street, cp, cityId, onSuccess, onErrors) =>
appFetch(`/users/favouriteAddresses`,
    config('POST', {street, cp, cityId}),
    onSuccess, onErrors);

export const deleteFavouriteAddress = (addressId, onSuccess, onErrors) =>
    appFetch(`/users/favouriteAddresses/${addressId}`,
        config('DELETE'), onSuccess, onErrors);

export const findFavouriteAddresses = ({page}, onSuccess) => 
    appFetch(`/users/favouriteAddresses?page=${page}`, 
        config('GET'), onSuccess);

export const findFavAddress = (addressId, onSuccess) => 
    appFetch(`/users/favouriteAddress/${addressId}`, 
        config('GET'), onSuccess);

export const findFavouriteAddressesByCity = (cityId, {page}, onSuccess) => 
    appFetch(`/users/favouriteAddressesByCity?cityId=${cityId}&page=${page}`, 
        config('GET'), onSuccess);
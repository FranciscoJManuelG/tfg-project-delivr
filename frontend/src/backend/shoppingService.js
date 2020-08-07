import {config, appFetch} from './appFetch';

export const addToShoppingCart = (shoppingCartId, productId, companyId, quantity, onSuccess, 
    onErrors) =>
    appFetch(`/shopping/shoppingCarts/${shoppingCartId}/addToShoppingCart`, 
        config('POST', {productId, companyId, quantity}), onSuccess, onErrors);

export const updateShoppingCartItemQuantity = (shoppingCartId, productId, companyId,
    quantity, onSuccess, onErrors) =>
    appFetch(`/shopping/shoppingCarts/${shoppingCartId}/updateShoppingCartItemQuantity`, 
        config('POST', {productId, companyId, quantity}), onSuccess, onErrors);

export const removeShoppingCartItem = (shoppingCartId, productId, companyId, onSuccess,
    onErrors) =>
    appFetch(`/shopping/shoppingCarts/${shoppingCartId}/removeShoppingCartItem`, 
        config('POST', {productId, companyId}), onSuccess, onErrors);

export const findShoppingCartProducts = (shoppingCartId, companyId,  onSuccess) => 
    appFetch(`/shopping/shoppingCarts/${shoppingCartId}?companyId=${companyId}`, 
        config('GET'), onSuccess);
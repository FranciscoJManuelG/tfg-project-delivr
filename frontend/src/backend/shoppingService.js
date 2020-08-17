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

export const changeShoppingCartHomeSale = (shoppingCartId, companyId, homeSale,
    onSuccess, onErrors) =>
    appFetch(`/shopping/shoppingCarts/${shoppingCartId}/changeShoppingCartHomeSale`, 
        config('POST', {companyId, homeSale}), onSuccess, onErrors);
        

export const buy = (shoppingCartId, companyId, homeSale, street, cp,
    onSuccess, onErrors) =>
    appFetch(`/shopping/shoppingCarts/${shoppingCartId}/buy`, 
        config('POST', {companyId, homeSale, street, cp}), onSuccess, onErrors);

export const findUserOrders = ({page}, onSuccess) => 
    appFetch(`/shopping/userOrders?page=${page}`, config('GET'), onSuccess);

export const findCompanyOrders = (companyId, {page}, onSuccess) => 
    appFetch(`/shopping/companyOrders?companyId=${companyId}&page=${page}`, config('GET'), onSuccess);

export const findOrder = (orderId, onSuccess) =>
    appFetch(`/shopping/orders/${orderId}`, config('GET'), onSuccess);
        
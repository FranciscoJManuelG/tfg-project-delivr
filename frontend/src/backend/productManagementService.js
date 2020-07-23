import {config, appFetch} from './appFetch';

export const addProduct = (companyId, name, description, price, path, productCategoryId, 
    onSuccess, onErrors) =>
    appFetch(`/management/products`,
        config('POST', {companyId, name, description, price, path, productCategoryId}),
        onSuccess, onErrors);

export const editProduct = (product, onSuccess, onErrors) =>
    appFetch(`/management/products/${product.id}`, config('PUT', product),
        onSuccess, onErrors);

export const removeProduct = (productId, companyId, onSuccess, onErrors) =>
    appFetch(`/management/products/${productId}`, 
        config('DELETE', companyId), onSuccess, onErrors);

export const blockProduct = (productId, companyId, onSuccess) =>
    appFetch(`/management/products/${productId}/block`, 
        config('POST', companyId), onSuccess);

export const unlockCompany = (productId, companyId, onSuccess) =>
    appFetch(`/management/products/${productId}/unlock`, 
        config('POST', companyId), onSuccess);

export const findAllProductCategories = (onSuccess) => 
    appFetch('/management/products/categories', 
        config('GET'), onSuccess);

export const findAllCompanyProducts = (companyId, onSuccess) => 
    appFetch(`/management/products/${companyId}`, 
        config('GET'), onSuccess);
    
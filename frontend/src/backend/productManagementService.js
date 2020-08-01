import {config, appFetch} from './appFetch';

export const addProduct = (companyId, name, description, price, file, productCategoryId, 
    onSuccess, onErrors) =>{

        let path = `/management/products`;

        path += file ? `?file=${file}` : "";

        appFetch(path, config('POST', {companyId, name, description, price, productCategoryId}),
            onSuccess, onErrors);

    }

export const editProduct = (product, file, onSuccess, onErrors) =>{

    let path = `/management/products/${product.id}`;

    path += file ? `?file=${file}` : "";

    appFetch(path, config('PUT', product),
        onSuccess, onErrors);
}

export const removeProduct = (productId, companyId, onSuccess, onErrors) =>
    appFetch(`/management/products/${productId}?companyId=${companyId}`, 
        config('DELETE'), onSuccess, onErrors);

export const blockProduct = (productId, companyId, onSuccess) =>
    appFetch(`/management/products/${productId}/block`, 
        config('POST', {companyId}), onSuccess);

export const unlockProduct = (productId, companyId, onSuccess) =>
    appFetch(`/management/products/${productId}/unlock`, 
        config('POST', {companyId}), onSuccess);

export const findProduct = (productId, onSuccess) => 
    appFetch(`/management/products/${productId}`, 
        config('GET'), onSuccess);

export const findAllProductCategories = (onSuccess) => 
    appFetch('/management/products/categories', 
        config('GET'), onSuccess);

export const findAllCompanyProducts = (companyId, onSuccess) => 
    appFetch(`/management/products?companyId=${companyId}`, 
        config('GET'), onSuccess);
    
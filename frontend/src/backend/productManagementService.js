import {config, appFetch} from './appFetch';

export const addProduct = (companyId, name, description, price, file, productCategoryId, 
    onSuccess, onErrors) =>{

        let path = `/management/products`;

        const formData = new FormData();
        formData.append('file', file);
        formData.append('data', new Blob([JSON.stringify({ name, description, price, companyId, productCategoryId})], {type: "application/json"} ));

        appFetch(path, config('POST', formData),
            onSuccess, onErrors);

    }

export const editProduct = (productId, companyId, name, description, price, file, productCategoryId, onSuccess, onErrors) =>{

    let path = `/management/products/${productId}`;

    const formData = new FormData();
        formData.append('file', file);
        formData.append('data', new Blob([JSON.stringify({ name, description, price, companyId, productCategoryId})], {type: "application/json"} ));

    appFetch(path, config('PUT', formData),
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

        export const addProductCategory = (name, onSuccess, onErrors) =>
    appFetch(`/management/productCategories`,
        config('POST', {name}), onSuccess, onErrors);

export const modifyProductCategory = (productCategory, onSuccess, onErrors) =>
    appFetch(`/management/productCategories/${productCategory.id}`, config('PUT', productCategory),
        onSuccess, onErrors);

    
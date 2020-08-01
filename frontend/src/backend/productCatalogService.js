import {config, appFetch} from './appFetch';

export const findProducts = (companyId, {productCategoryId, keywords}, 
    onSuccess) => {

    let path = `/productCatalog/products?companyId=${companyId}`;

    path += productCategoryId ? `&productCategoryId=${productCategoryId}` : "";
    path += keywords.length > 0 ? `&keywords=${keywords}` : "";

    appFetch(path, config('GET'), onSuccess);

}

export const findCompanyProductCategories = (companyId, onSuccess) => 
    appFetch(`/productCatalog/products/categories?companyId=${companyId}`, 
        config('GET'), onSuccess);

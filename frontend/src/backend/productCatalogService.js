import {config, appFetch} from './appFetch';

export const findProducts = (companyId, {productCategoryId, keywords}, 
    onSuccess) => {

    let path = `/productCatalog/products/${companyId}`;

    path += productCategoryId || keywords.length > 0 ? `?` : "";
    path += keywords.length === 0 && productCategoryId ? `productCategoryId=${productCategoryId}` : "";
    path += productCategoryId == null && keywords.length > 0 ? `keywords=${keywords}` : "";
    path += productCategoryId && keywords.length > 0 ? `productCategoryId=${productCategoryId}&keywords=${keywords}` : "";

    appFetch(path, config('GET'), onSuccess);

}

export const findCompanyProductCategories = (companyId, onSuccess) => 
    appFetch(`/productCatalog/products/${companyId}/categories`, 
        config('GET'), onSuccess);

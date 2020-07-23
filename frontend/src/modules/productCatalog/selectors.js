const getModuleState = state => state.productCatalog

export const getProductCategories = state =>
    getModuleState(state).productCategories;

export const getProductCategoryName = (productCategories, id) => {
    
    if (!productCategories){
        return '';
    }
    
    const productCategory = productCategories.find(productCategory => productCategory.id === id);
    
    if (!productCategory){
        return '';
    }

    return productCategory.name;
}

export const getProductSearch = state =>
    getModuleState(state).productSearch;

import * as actionTypes from './actionTypes';
import backend from '../../backend';
import * as selectors from './selectors';

const clearProductSearch = () => ({
    type: actionTypes.CLEAR_PRODUCT_SEARCH
});

const findProductsCompleted = productSearch => ({
    type: actionTypes.FIND_PRODUCTS_COMPLETED,
    productSearch
});

export const findProducts = (companyId, criteria) => dispatch => {

    dispatch(clearProductSearch());
    backend.productCatalogService.findProducts(companyId, criteria,
        result => dispatch(findProductsCompleted({criteria, result})));

}

const findCompanyProductCategoriesCompleted = productCategories => ({
    type: actionTypes.FIND_COMPANY_PRODUCT_CATEGORIES_COMPLETED,
    productCategories
});

export const findCompanyProductCategories = (companyId) => (dispatch, getState) => {

    const productCategories = selectors.getProductCategories(getState());

    if (!productCategories) {

        backend.productCatalogService.findCompanyProductCategories(companyId,
            productCategories => dispatch(findCompanyProductCategoriesCompleted(productCategories)));

    }

}

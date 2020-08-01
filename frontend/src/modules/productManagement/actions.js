import * as actionTypes from './actionTypes';
import backend from '../../backend';
import * as selectors from './selectors';

const addedProductCompleted = product => ({
    type: actionTypes.ADDED_PRODUCT_COMPLETED,
    product
});

export const addProduct = (companyId, name, description, price, file, productCategoryId,
    onSuccess, onErrors) => dispatch =>
    backend.productManagementService.addProduct(companyId, name, description, price, file, productCategoryId,
        product => {
            dispatch(addedProductCompleted(product));
            onSuccess();
        },
        onErrors);

const findProductsCompleted = productSearch => ({
    type: actionTypes.FIND_PRODUCTS_COMPLETED,
    productSearch
});

const clearProductSearch = () => ({
    type: actionTypes.CLEAR_PRODUCT_SEARCH
});


export const findProducts = (companyId) => dispatch => {

    dispatch(clearProductSearch());
    backend.productManagementService.findAllCompanyProducts(companyId,
        productSearch => dispatch(findProductsCompleted(productSearch)));

}

const findAllProductCategoriesCompleted = productCategories => ({
    type: actionTypes.FIND_ALL_PRODUCT_CATEGORIES_COMPLETED,
    productCategories
});

export const findAllProductCategories = () => (dispatch, getState) => {

    const productCategories = selectors.getProductCategories(getState());

    if (!productCategories) {

        backend.productManagementService.findAllProductCategories(
            productCategories => dispatch(findAllProductCategoriesCompleted(productCategories))
        );

    }

}

const productDeleted = productId => ({
    type: actionTypes.PRODUCT_DELETED,
    productId
});

export const removeProduct = (companyId, productId, onSuccess, 
    onErrors) => dispatch => 
    backend.productManagementService.removeProduct(productId, companyId,
        () => {
            dispatch(productDeleted(productId));
            onSuccess();
        },
        onErrors);

const blockProductCompleted = product => ({
    type: actionTypes.BLOCK_PRODUCT_COMPLETED,
    product
})

export const blockProduct = (companyId, productId, onSuccess) => dispatch =>
    backend.productManagementService.blockProduct(companyId, productId,
    product => {
        dispatch(blockProductCompleted(product));
        onSuccess();
    });

const unlockProductCompleted = product => ({
    type: actionTypes.UNLOCK_PRODUCT_COMPLETED,
    product
})

export const unlockProduct = (companyId, productId, onSuccess) => dispatch =>
    backend.productManagementService.unlockProduct(companyId, productId, 
    product => {
        dispatch(unlockProductCompleted(product));
        onSuccess();
    });

const editProductCompleted = product => ({
    type: actionTypes.EDIT_PRODUCT_COMPLETED,
    product
})

export const editProduct = (product, file, onSuccess, onErrors) => dispatch =>
    backend.productManagementService.editProduct(product, file,
        product => {
            dispatch(editProductCompleted(product));
            onSuccess();
        },
        onErrors);

const findProductCompleted = product => ({
    type: actionTypes.FIND_PRODUCT_COMPLETED,
    product
});

export const clearProduct = () => ({
    type: actionTypes.CLEAR_PRODUCT
});

export const findProduct = productId => dispatch => {
    dispatch(clearProduct());
    backend.productManagementService.findProduct(productId, product => {
        dispatch(findProductCompleted(product));
    });
}
        
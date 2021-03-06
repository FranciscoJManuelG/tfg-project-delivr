import * as actionTypes from './actionTypes';
import backend from '../../backend';
import * as selectors from './selectors';

const addedCompanyCompleted = company => ({
    type: actionTypes.ADDED_COMPANY_COMPLETED,
    company
});

export const addCompany = (name, capacity, reserve, homeSale, reservePercentage, companyCategoryId, 
    onSuccess, onErrors) => dispatch =>
    backend.businessService.addCompany(name, capacity, reserve, homeSale, reservePercentage, companyCategoryId, 
        company => {
            dispatch(addedCompanyCompleted(company));
            onSuccess();
        },
        onErrors);

const modifyCompanyCompleted = company => ({
    type: actionTypes.MODIFY_COMPANY_COMPLETED,
    company
})

export const modifyCompany = (company, onSuccess, onErrors) => dispatch =>
    backend.businessService.modifyCompany(company, 
        company => {
            dispatch(modifyCompanyCompleted(company));
            onSuccess();
        },
        onErrors);

const blockCompanyCompleted = company => ({
    type: actionTypes.BLOCK_COMPANY_COMPLETED,
    company
})

export const blockCompany = (companyId, onSuccess) => dispatch =>
    backend.businessService.blockCompany(companyId, 
    company => {
        dispatch(blockCompanyCompleted(company));
        onSuccess();
    });

const unlockCompanyCompleted = company => ({
    type: actionTypes.UNLOCK_COMPANY_COMPLETED,
    company
})

export const unlockCompany = (companyId, onSuccess) => dispatch =>
    backend.businessService.unlockCompany(companyId, 
    company => {
        dispatch(unlockCompanyCompleted(company));
        onSuccess();
    });

const findAllCompanyCategoriesCompleted = companyCategories => ({
    type: actionTypes.FIND_ALL_COMPANY_CATEGORIES_COMPLETED,
    companyCategories
});

export const findAllCompanyCategories = () => (dispatch, getState) => {

    const companyCategories = selectors.getCompanyCategories(getState());

    if (!companyCategories) {

        backend.businessService.findAllCompanyCategories(
            companyCategories => dispatch(findAllCompanyCategoriesCompleted(companyCategories))
        );

    }

}

const findCompanyAddressesCompleted = companyAddressSearch => ({
    type: actionTypes.FIND_COMPANY_ADDRESSES_COMPLETED,
    companyAddressSearch
});

const clearCompanyAddressSearch = () => ({
    type: actionTypes.CLEAR_COMPANY_ADDRESS_SEARCH
});


export const findCompanyAddresses = (companyId, criteria) => dispatch => {

    dispatch(clearCompanyAddressSearch());
    backend.businessService.findCompanyAddresses(companyId, criteria, 
        result => dispatch(findCompanyAddressesCompleted({criteria, result})));

}

export const previousFindCompanyAddressesResultPage = (companyId, criteria) => 
    findCompanyAddresses(companyId, {page: criteria.page-1});

export const nextFindCompanyAddressesResultPage = (companyId, criteria) => 
    findCompanyAddresses(companyId, {page: criteria.page+1});

const addedCompanyAddressCompleted = address => ({
    type: actionTypes.ADDED_COMPANY_ADDRESS_COMPLETED,
    address
});

export const addCompanyAddress = (street, cp, cityId, companyId, onSuccess, onErrors) => dispatch =>
    backend.businessService.addCompanyAddress(street, cp, cityId, companyId, 
        address => {
            dispatch(addedCompanyAddressCompleted(address));
            onSuccess();
        },
        onErrors);

const companyAddressDeleted = addressId => ({
    type: actionTypes.COMPANY_ADDRESS_DELETED,
    addressId
});

export const deleteCompanyAddress = (addressId, onSuccess, 
    onErrors) => dispatch => 
    backend.businessService.deleteCompanyAddress(addressId,
        () => {
            dispatch(companyAddressDeleted(addressId));
            onSuccess();
        },
        onErrors);

const findAllCitiesCompleted = cities => ({
    type: actionTypes.FIND_ALL_CITIES_COMPLETED,
    cities
});

export const findAllCities = () => (dispatch, getState) => {

    const cities = selectors.getCities(getState());

    if (!cities) {

        backend.businessService.findAllCities(
            cities => dispatch(findAllCitiesCompleted(cities))
        );

    }

}

export const findCompanyCompleted = company => ({
    type: actionTypes.FIND_COMPANY_COMPLETED,
    company
})
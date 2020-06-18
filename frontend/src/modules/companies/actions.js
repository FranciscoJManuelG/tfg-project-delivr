import * as actionTypes from './actionTypes';
import backend from '../../backend';
import * as selectors from './selectors';

const addedCompanyCompleted = companyId => ({
    type: actionTypes.ADDED_COMPANY_COMPLETED,
    companyId
});

export const addCompany = (name, capacity, reserve, homeSale, reservePercentage, companyCategoryId, 
    onSuccess, onErrors) => dispatch =>
    backend.companyService.addCompany(name, capacity, reserve, homeSale, reservePercentage, companyCategoryId, 
        ({id}) => {
            dispatch(addedCompanyCompleted(id));
            onSuccess();
        },
        onErrors);

const findAllCompanyCategoriesCompleted = companyCategories => ({
    type: actionTypes.FIND_ALL_COMPANY_CATEGORIES_COMPLETED,
    companyCategories
});

export const findAllCompanyCategories = () => (dispatch, getState) => {

    const companyCategories = selectors.getCompanyCategories(getState());

    if (!companyCategories) {

        backend.companyService.findAllCompanyCategories(
            companyCategories => dispatch(findAllCompanyCategoriesCompleted(companyCategories))
        );

    }

}
        
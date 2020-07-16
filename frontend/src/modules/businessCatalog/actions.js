import * as actionTypes from './actionTypes';
import backend from '../../backend';

const clearCompanySearch = () => ({
    type: actionTypes.CLEAR_COMPANY_SEARCH
});

const findCompaniesCompleted = companySearch => ({
    type: actionTypes.FIND_COMPANIES_COMPLETED,
    companySearch
});

export const findCompanies = criteria => dispatch => {

    dispatch(clearCompanySearch());
    backend.businessCatalogService.findCompanies(criteria,
        result => dispatch(findCompaniesCompleted({criteria, result})));

}

export const previousFindCompaniesResultPage = criteria =>
    findCompanies({...criteria, page: criteria.page-1});

export const nextFindCompaniesResultPage = criteria =>
    findCompanies({...criteria, page: criteria.page+1});


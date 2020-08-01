import {config, appFetch} from './appFetch';

export const addCompany = (name, capacity, reserve, homeSale, reservePercentage, companyCategoryId, 
    onSuccess, onErrors) =>
    appFetch(`/business/companies`,
        config('POST', {name, capacity, reserve, homeSale, reservePercentage, companyCategoryId}),
        onSuccess, onErrors);

export const modifyCompany = (company, onSuccess, onErrors) =>
    appFetch(`/business/companies/${company.id}`, config('PUT', company),
        onSuccess, onErrors);

export const blockCompany = (companyId, onSuccess) =>
    appFetch(`/business/companies/${companyId}/block`, 
        config('POST'), onSuccess);

export const unlockCompany = (companyId, onSuccess) =>
    appFetch(`/business/companies/${companyId}/unlock`, 
        config('POST'), onSuccess);

export const deregister = (companyId, onSuccess, onErrors) =>
    appFetch(`/business/companies/${companyId}`, 
        config('DELETE'), onSuccess, onErrors);

export const findCompany = (onSuccess) => 
    appFetch('/business/companies/company', 
        config('GET'), onSuccess);

export const findAllCompanyCategories = (onSuccess) => 
    appFetch('/business/companies/categories', 
        config('GET'), onSuccess);

export const addCompanyAddress = (street, cp, cityId, companyId, onSuccess, onErrors) =>
    appFetch(`/business/companyAddresses`,
        config('POST', {street, cp, cityId, companyId}),
        onSuccess, onErrors);

export const deleteCompanyAddress = (addressId, onSuccess, onErrors) =>
    appFetch(`/business/companyAddresses/${addressId}`,
        config('DELETE'), onSuccess, onErrors);

export const findCompanyAddresses = (companyId, {page}, onSuccess) => 
    appFetch(`/business/companyAddresses?companyId=${companyId}&page=${page}`, 
        config('GET'), onSuccess);

export const findAllCities = (onSuccess) => 
    appFetch('/business/cities', 
        config('GET'), onSuccess);
    
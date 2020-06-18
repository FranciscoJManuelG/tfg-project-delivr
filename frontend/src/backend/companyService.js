import {config, appFetch} from './appFetch';

export const addCompany = (name, capacity, reserve, homeSale, reservePercentage, companyCategoryId, 
    onSuccess, onErrors) =>
    appFetch(`/companies/add`,
        config('POST', {name, capacity, reserve, homeSale, reservePercentage, companyCategoryId}),
        onSuccess, onErrors);

export const modifyCompany = (company, onSuccess, onErrors) =>
    appFetch(`/companies/${company.id}`, config('PUT', company),
        onSuccess, onErrors);

export const deregister = (id, onSuccess, onErrors) =>
    appFetch(`/companies/${id}/deregister`, 
        config('POST'), onSuccess, onErrors);

export const findAllCompanyCategories = (onSuccess) => 
appFetch('/companies/categories', config('GET'), onSuccess);
    
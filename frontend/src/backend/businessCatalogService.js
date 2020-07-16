import {config, appFetch} from './appFetch';

export const findCompanies = ({companyCategoryId, cityId, street, keywords, page}, 
    onSuccess) => {

    let path = `/businessCatalog/companies?page=${page}`;

    path += companyCategoryId ? `&companyCategoryId=${companyCategoryId}` : "";
    path += cityId ? `&cityId=${cityId}` : "";
    path += street.length > 0 ? `&street=${street}` : "";
    path += keywords.length > 0 ? `&keywords=${keywords}` : "";

    appFetch(path, config('GET'), onSuccess);

}

import React from 'react';
import {useSelector} from 'react-redux';
import {Link} from 'react-router-dom';

import * as selectors from '../selectors';
import CompanyCategories from './CompanyCategories';

const FindCompanyCategoriesResult = () => {

    const companyCategories = useSelector(selectors.getCompanyCategories);

    if (!companyCategories) {
        return null;
    }

    if (companyCategories.length === 0) {
        return (
            <div>
                <div className="alert alert-info" role="alert">
                    No existe categorías de empresa
                </div>
                <Link to='/business/add-company-category'>
                    <div className="form-group">
                        <button type="submit" className="btn btn-primary">Añadir</button>
                    </div> 
                </Link>
            </div>
        );
    }

    return (

        <div>
            <CompanyCategories companyCategories={companyCategories}/>
            <Link to='/business/add-company-category'>
                <div className="vertical-center">
                    <button type="submit" className="btn btn-dark">Añadir</button>
                </div> 
            </Link>
        </div>

    );

}

export default FindCompanyCategoriesResult;

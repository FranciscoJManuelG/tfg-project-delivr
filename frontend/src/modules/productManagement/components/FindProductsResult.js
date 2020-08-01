import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

import * as selectors from '../selectors';
import * as businessSelectors from '../../business/selectors';
import * as actions from '../actions';
import Products from './Products';

const FindProductsResult = () => {

    const productSearch = useSelector(selectors.getProductSearch);
    const productCategories = useSelector(selectors.getProductCategories);
    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();

    if (!productSearch) {
        return null;
    }

    if (productSearch.length === 0) {
        return (
            <div>
                <Link to='/management/add-product'>
                    <div className="form-group">
                        <button type="submit" className="btn btn-primary">Añadir</button>
                    </div> 
                </Link>
                <div className="alert alert-info" role="alert">
                    <FormattedMessage id='project.business.FindCompaniesResult.noCompanies'/>
                </div>
            </div>
        );
    }

    return (

        <div>
            <Link to='/management/add-product'>
                <div className="form-group">
                    <button type="submit" className="btn btn-primary">Añadir</button>
                </div> 
            </Link>
            <Products products={productSearch} productCategories={productCategories} company={company}
                onDeleteItem={(...args) => dispatch(actions.removeProduct(...args))}
                onBlockItem={(...args) => dispatch(actions.blockProduct(...args))}
                onUnlockItem={(...args) => dispatch(actions.unlockProduct(...args))}/>
        </div>

    );

}

export default FindProductsResult;

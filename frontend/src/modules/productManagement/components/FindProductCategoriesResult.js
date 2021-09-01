import React from 'react';
import {useSelector} from 'react-redux';
import {Link} from 'react-router-dom';

import * as selectors from '../selectors';
import ProductCategories from './ProductCategories';

const FindProductCategoriesResult = () => {

    const productCategories = useSelector(selectors.getProductCategories);

    if (!productCategories) {
        return null;
    }

    if (productCategories.length === 0) {
        return (
            <div>
                <div className="alert alert-info" role="alert">
                    No existe categorías de productos
                </div>
                <Link to='/business/add-product-category'>
                    <div className="form-group">
                        <button type="submit" className="btn btn-primary">Añadir</button>
                    </div> 
                </Link>
            </div>
        );
    }

    return (

        <div>
            <ProductCategories productCategories={productCategories}/>
            <Link to='/business/add-product-category'>
                <div className="vertical-center">
                    <button type="submit" className="btn btn-dark">Añadir</button>
                </div> 
            </Link>
        </div>

    );

}

export default FindProductCategoriesResult;

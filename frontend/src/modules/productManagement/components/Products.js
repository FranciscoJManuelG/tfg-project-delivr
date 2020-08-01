import React, {useState} from 'react';
import PropTypes from 'prop-types';

import Product from './Product';
import {Errors} from '../../common';

const Products = ({products, productCategories, company, onDeleteItem, onBlockItem, onUnlockItem}) => {

    const [backendErrors, setBackendErrors] = useState(null);

    return(

        <div>

            <Errors errors={backendErrors}
                onClose={() => setBackendErrors(null)}/>

            {products.map(product => 
                <Product key={product.id}
                    product={product}
                    productCategories={productCategories}
                    company={company}
                    onDeleteItem={onDeleteItem}
                    onBlockItem={onBlockItem}
                    onUnlockItem={onUnlockItem}
                    onBackendErrors={errors => setBackendErrors(errors)}/>
            )}

        </div>

    );

};

Products.propTypes = {
    products: PropTypes.array.isRequired,
    productCategories: PropTypes.array.isRequired
};

export default Products;

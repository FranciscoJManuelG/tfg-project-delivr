import React from 'react';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import ProductCategory from './ProductCategory';

const ProductCategories = ({productCategories}) => {

    return (

        <div>

            <table className="table table-hover">

                <thead>
                    <tr>
                        <th scope="col">
                            <FormattedMessage id='project.global.fields.name'/>
                        </th>
                        <th scope="col" style={{width: '20%'}}></th>
                    </tr>
                </thead>

                <tbody>
                    {productCategories.map(productCategory => 
                        <ProductCategory key={productCategory.id}
                            productCategory={productCategory}
                            />
                    )}
                </tbody>

            </table>
        </div>
    );

}

ProductCategories.propTypes = {
    productCategories: PropTypes.array.isRequired,
};

export default ProductCategories;


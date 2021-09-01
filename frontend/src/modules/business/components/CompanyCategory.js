import React from 'react';
import PropTypes from 'prop-types';

import {Link} from 'react-router-dom';


const CompanyCategory = ({companyCategory}) => {

    return (
        <tr>                   
            <td>
                {companyCategory.name}
            </td>
            <td>
                <Link to={`/business/find-company-category-to-edit/${companyCategory.id}`}>
                        <div className="form-group">
                            <button type="submit" className="btn btn-primary btn-sm float-center">
                                <span className="fas fa-edit"></span>
                            </button>
                        </div> 
                </Link>
            </td>
        </tr>
    );

}

CompanyCategory.propTypes = {
    companyCategory: PropTypes.object.isRequired
}

export default CompanyCategory;

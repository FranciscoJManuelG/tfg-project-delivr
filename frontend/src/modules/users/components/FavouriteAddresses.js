import React, {useState} from 'react';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import FavouriteAddress from './FavouriteAddress';
import {Errors} from '../../common';

const FavouriteAddresses = ({favouriteAddresses, cities, onDeleteItem}) => {

    const [backendErrors, setBackendErrors] = useState(null);

    return (

        <div>

            <Errors errors={backendErrors}
                onClose={() => setBackendErrors(null)}/>

            <table className="table table-striped table-hover">

                <thead>
                    <tr>
                        <th scope="col">
                            <FormattedMessage id='project.global.fields.street'/>
                        </th>
                        <th scope="col">
                            <FormattedMessage id='project.global.fields.cp'/>
                        </th>
                        <th scope="col">
                            <FormattedMessage id='project.global.fields.city'/>
                        </th>
                        <th scope="col" style={{width: '60%'}}></th>
                    </tr>
                </thead>

                <tbody>
                    {favouriteAddresses.map(favouriteAddress => 
                        <FavouriteAddress key={favouriteAddress.id}
                            favouriteAddress={favouriteAddress}
                            cities={cities}
                            onDeleteItem={onDeleteItem}
                            onBackendErrors={errors => setBackendErrors(errors)}
                            />
                    )}
                </tbody>

            </table>
        </div>
    );

}

FavouriteAddresses.propTypes = {
    favouriteAddresses: PropTypes.array.isRequired,
    cities: PropTypes.array.isRequired,
    onDeleteItem: PropTypes.func
};

export default FavouriteAddresses;


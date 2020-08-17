import React from 'react';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import FavAddress from './FavAddress';

const FavAddresses = ({favouriteAddresses, cities, companyId, onSelectItem}) => {

    return (

        <div>

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
                        <FavAddress key={favouriteAddress.id}
                            favouriteAddress={favouriteAddress}
                            cities={cities}
                            companyId={companyId}
                            onSelectItem={onSelectItem}
                            />
                    )}
                </tbody>

            </table>
        </div>
    );

}

FavAddresses.propTypes = {
    favouriteAddresses: PropTypes.array.isRequired,
    cities: PropTypes.array.isRequired,
    onSelectItem: PropTypes.func
};

export default FavAddresses;


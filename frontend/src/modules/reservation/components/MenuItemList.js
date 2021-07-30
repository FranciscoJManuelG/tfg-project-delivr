import React, {useState} from 'react';
import PropTypes from 'prop-types';
import {FormattedMessage, FormattedNumber} from 'react-intl';

import MenuItem from './MenuItem';
import {Errors} from '../../common';

const MenuItemList = ({list, edit, companyId, onUpdateQuantity, onRemoveItem}) => {

    const [backendErrors, setBackendErrors] = useState(null);

    if (list.items.length === 0) {
        return (
            <div className="alert alert-info" role="alert">
                <FormattedMessage id='project.reservation.Menu.empty'/>
            </div>
        );
    }

    return (

        <div>

            <Errors errors={backendErrors}
                onClose={() => setBackendErrors(null)}/>

            <table className="table">

                <thead>
                    <tr>
                        <th scope="col" style={{width: '60%'}}></th>
                        <th scope="col" style={{width: '20%'}}>
                            <FormattedMessage id='project.global.fields.price'/>
                        </th>
                        <th scope="col" style={{width: '20%'}}>
                            <FormattedMessage id='project.global.fields.quantity'/>
                        </th>
                    </tr>
                </thead>

                <tbody>
                    {list.items.map(item => 
                        <MenuItem menuItemListId={list.id}
                            key={item.productId} item={item}
                            edit={edit} 
                            companyId={companyId}
                            onUpdateQuantity={onUpdateQuantity}
                            onRemoveItem={onRemoveItem}
                            onBackendErrors={errors => setBackendErrors(errors)}/>
                    )}
                </tbody>

            </table>

            <p className="text-center font-weight-bold">
                <FormattedMessage id='project.global.fields.totalPrice'/> 
                : <FormattedNumber value={list.totalPrice}/>€
            </p>

        </div>

    );

}

MenuItemList.propTypes = {
    list: PropTypes.object.isRequired,
    edit: PropTypes.bool,
    onUpdateQuantity: PropTypes.func,
    onRemoveItem: PropTypes.func
}

export default MenuItemList;

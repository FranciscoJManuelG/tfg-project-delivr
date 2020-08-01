import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {useHistory, useParams} from 'react-router-dom';

import * as actions from '../actions';

const FindProductsToEdit = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const {id} = useParams();

    useEffect(() => {

        dispatch(actions.findProduct(id));
        history.push(`/management/edit-product`);
        
        

    });

    return null;

}

export default FindProductsToEdit;

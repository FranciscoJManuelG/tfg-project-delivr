import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';

const FindAllCompaniesCriteria = () => {

    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {

        dispatch(actions.findAllCompanies({
            keywords: "", 
            page: 0}));
        history.push('/businessCatalog/find-all-companies-result');

    }, [dispatch, history]);

    return null;

}

export default FindAllCompaniesCriteria;

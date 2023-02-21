import { MailOutlined, AppstoreOutlined, SettingOutlined } from '@ant-design/icons';
import { autocompleteClasses } from '@mui/material';
import { Menu } from 'antd';
import { Navbar, Nav } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';


function getItem(label, key, icon, children, type) {
  return {
    key,
    icon,
    children,
    label,
    type,
  };
}
const items = [
  getItem('거래소', 'sub1', <MailOutlined />),
  getItem('매매일지', 'sub2', <AppstoreOutlined />),
  getItem('커뮤니티', 'sub3', <SettingOutlined />)
];

const SideMenu = () => {
  const navigate = useNavigate();

  const onClick = (e) => {
    if (e.key === 'sub1'){
      navigate('/upbitMainPage');
    }
    else if (e.key === 'sub2'){
      navigate('/diary');
    }
    else if (e.key === 'sub3'){
      navigate('/community');
    }
  };
  return (
    <>
    <Menu
      onClick={onClick}
      style={{width: '100%', height: '100vh'}}
      defaultSelectedKeys={['1']}
      defaultOpenKeys={['sub1']}
      mode="inline"
      items={items}>
    </Menu>
    </>
  );
};
export default SideMenu;

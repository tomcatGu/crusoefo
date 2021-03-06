import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: 'Dashboard', icon: 'dashboard' }
    }]
  },
  {
    path: '/modeler',
    component: Layout,
    name: 'Modeler',
    meta: { title: 'Modeler', icon: 'example' },
    children: [{
      path: 'bpmn',
      name: 'BPMN',
      component: () => import('@/views/modeler/bpmn/index'),
      meta: { title: 'Bpmn Modeler', icon: 'dashboard' }
    },
    {
      path: 'bpmn-edit/:id',
      name: 'BPMN EDIT',
      component: () => import('@/views/modeler/bpmn/edit/index'),
      meta: { title: 'Edit Bpmn Modeler', icon: 'dashboard' },
      hidden: true
    },
    {
      path: 'cmmn',
      name: 'CMMN',
      component: () => import('@/views/modeler/cmmn/index'),
      meta: { title: 'Cmmn Modeler', icon: 'dashboard' }
    }
    ]
  },
  {
    path: '/example',
    component: Layout,
    redirect: '/example/table',
    name: 'Example',
    meta: { title: 'Example', icon: 'example' },
    children: [
      {
        path: 'table',
        name: 'Table',
        component: () => import('@/views/table/index'),
        meta: { title: 'Table', icon: 'table' }
      },
      {
        path: 'tree',
        name: 'Tree',
        component: () => import('@/views/tree/index'),
        meta: { title: 'Tree', icon: 'tree' }
      }
    ]
  },

  {
    path: '/form',
    component: Layout,
    meta: { title: 'Form', icon: 'form' },
    children: [
      {
        path: 'form-test',
        name: 'Form',
        component: () => import('@/views/form/index'),
        meta: { title: 'Form', icon: 'form' }
      },
      {
        path: 'form-create',
        name: 'Create Form',
        component: () => import('@/views/form/create/index'),
        meta: { title: 'Create Form', icon: 'form' }
      },
      {
        path: 'form-edit/:id/:resourceId',
        name: 'Edit Form',
        component: () => import('@/views/form/edit/index'),
        meta: { title: 'Edit Form', icon: 'form' },
        hidden: true
      },
      {
        path: 'start-form/:id/',
        name: 'Start Form',
        component: () => import('@/views/form/start/index'),
        meta: { title: 'Start Form', icon: 'form' },
        hidden: true
      },
      {
        path: 'start-form/key/:key/',
        name: 'Start Form',
        component: () => import('@/views/form/start/index'),
        meta: { title: 'Start Form', icon: 'form' },
        hidden: true
      }
    ]
  },
  {
    path: '/camunda',
    component: Layout,
    meta: { title: 'Camunda', icon: 'form' },
    children: [
      {
        path: 'repository',
        name: 'Repository',
        component: () => import('@/views/camunda/repository/index'),
        meta: { title: 'Repository List', icon: 'form' }
      },
      {
        path: 'repository/resources/:id',
        name: 'Resources List',
        component: () => import('@/views/camunda/repository/resources/index'),
        meta: { title: 'Resources List', icon: 'form' },
        hidden: true
      },
      {
        path: 'processes',
        name: 'Processes',
        component: () => import('@/views/camunda/processes/index'),
        meta: { title: 'Processes List', icon: 'form' }
      },
      {
        path: 'processes/diagram/:id',
        name: 'Processes Diagram',
        component: () => import('@/views/camunda/processes/diagram/index'),
        meta: { title: 'Processes Diagram', icon: 'form' },
        hidden: true
      },
      {
        path: 'processInstance',
        name: 'Process Instance',
        component: () => import('@/views/camunda/processInstance/index'),
        meta: { title: 'ProcessInstance List', icon: 'form' }
      },
      {
        path: 'processInstance/:id/diagram/:definitionId',
        name: 'ProcessInstance Diagram',
        component: () => import('@/views/camunda/processInstance/diagram/index'),
        meta: { title: 'ProcessInstance Diagram', icon: 'form' },
        hidden: true
      },
      {
        path: 'task',
        name: 'Task',
        component: () => import('@/views/camunda/task/index'),
        meta: { title: 'Task List', icon: 'form' }
      },
      {
        path: 'task-form/:id/',
        name: 'Start Task',
        component: () => import('@/views/form/task/index'),
        meta: { title: 'Start Task', icon: 'form' },
        hidden: true
      }

    ]

  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  {
    path: '/user',
    component: Layout,
    alwaysShow: true,
    meta: {
      title: '用户管理',
      icon: 'example',
      roles: ['res1']
    },
    children: [
      {
        path: '/form/start-form/key/addUserProcess',
        name: 'Add User',
        meta: { title: '添加用户', icon: 'link', roles: ['res1'] }
      }
    ]

  },
  {
    path: '/nested',
    component: Layout,
    redirect: '/nested/menu1',
    name: 'Nested',
    meta: {
      title: 'Nested',
      icon: 'nested',
      roles: ['admin']
    },
    children: [
      {
        path: 'menu1',
        component: () => import('@/views/nested/menu1/index'), // Parent router-view
        name: 'Menu1',
        meta: { title: 'Menu1' },
        children: [
          {
            path: 'menu1-1',
            component: () => import('@/views/nested/menu1/menu1-1'),
            name: 'Menu1-1',
            meta: { title: 'Menu1-1' }
          },
          {
            path: 'menu1-2',
            component: () => import('@/views/nested/menu1/menu1-2'),
            name: 'Menu1-2',
            meta: { title: 'Menu1-2' },
            children: [
              {
                path: 'menu1-2-1',
                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-1'),
                name: 'Menu1-2-1',
                meta: { title: 'Menu1-2-1' }
              },
              {
                path: 'menu1-2-2',
                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-2'),
                name: 'Menu1-2-2',
                meta: { title: 'Menu1-2-2' }
              }
            ]
          },
          {
            path: 'menu1-3',
            component: () => import('@/views/nested/menu1/menu1-3'),
            name: 'Menu1-3',
            meta: { title: 'Menu1-3' }
          }
        ]
      },
      {
        path: 'menu2',
        component: () => import('@/views/nested/menu2/index'),
        meta: { title: 'menu2' }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
